angular.module('trainingTrackerApp')

    .controller('exerciseHistoryCtrl', ['$scope', '$state', '$timeout', 'recordsService', function ($scope, $state, $timeout, recordsService) {

        $scope.nameExercise = recordsService.getExerciseName();
        $scope.recordHistory = [];
        $scope.numPage = 1;

        // sort the [list] array alphabetically by date
        function sortCustom (list) {
            list.sort(function(a, b){
                var x = a.date.toLowerCase();
                var y = b.date.toLowerCase();
                if (x < y) {return -1;}
                if (x > y) {return 1;}
                return 0;
            });
        }

        // feedback handling variables
        $scope.errorMsg = "";
        $scope.error = false;

        // hide the error login message when is true respectively
        $scope.hideError = function () {
            $scope.errorMsg = "";
            $scope.error = false;
        };

        // show the error login message when is false respectively
        var showError = function (error) {
            $scope.errorMsg = error;
            $scope.error = true;
        };

        // get initial records
        $scope.getRecordHistoryList = function () {
            recordsService.getRecordHistoryList($scope.numPage, function (records) {
                $scope.recordHistory = $scope.recordHistory.concat(records);
                sortCustom($scope.recordHistory);
                $scope.numPage ++;
                $timeout(function(){
                    if (window.innerHeight == $(window).height()) {
                        $scope.getRecordHistoryList();
                    }
                },0,false);
            }, showError);
        };
        $scope.getRecordHistoryList();

        $scope.loadingRecords = false; // flag to indicate that client is charging a new page of records
        // scroll's watcher to get infinite scrolling
        $(document).scroll(function(e){

            if (!$scope.loadingRecords &&
                ($(window).scrollTop() +  window.innerHeight + 30 >= $(window).height())){

                $scope.loadingRecords = true;
                recordsService.getRecordHistoryList($scope.numPage, function (records) {
                    $scope.recordHistory = $scope.recordHistory.concat(records);
                    sortCustom($scope.recordHistory);
                    $scope.numPage ++;
                    $timeout(function(){
                        $scope.loadingRecords = false;
                    },0,false);
                }, showError);
            }
        });

    }]);

