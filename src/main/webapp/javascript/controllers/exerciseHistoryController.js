angular.module('trainingTrackerApp')

    .controller('exerciseHistoryCtrl', ['$scope', '$state', '$timeout', 'recordsService', function ($scope, $state, $timeout, recordsService) {

        $scope.nameExercise = recordsService.getExerciseName();
        $scope.idExercise = '../img/' + recordsService.getExerciseId() + '.png';
        $scope.recordHistory = [];
        $scope.numPage = 1;
        $scope.historyCompleted = false;

        // sort the [list] array numerically by date
        function sortCustom (list) {
            list.sort(function(a, b){
                var x = a.date.toLowerCase();
                var y = b.date.toLowerCase();
                if (x < y) {return 1;}
                if (x > y) {return -1;}
                return 0;
            });
        }

        // modify format date
        function formateDate (list) {
            for (i = 0; i < list.length; i++) {
                list[i].date = list[i].date.slice(0, 10);
            }
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

        // get initial records until 'window.innerHeight != $(window).height()'
        $scope.getRecordHistoryList = function () {
            recordsService.getRecordHistoryList($scope.numPage, function (records) {
                if (records.length > 0) {
                    $scope.recordHistory = $scope.recordHistory.concat(records);
                    formateDate($scope.recordHistory);
                    sortCustom($scope.recordHistory);
                    $scope.numPage ++;
                    $timeout(function(){
                        if (window.innerHeight == $(window).height()) {
                            $scope.getRecordHistoryList();
                        }
                    },0,false);
                } else {
                    $scope.historyCompleted = true;
                }
            }, showError);
        };
        $scope.getRecordHistoryList();

        $scope.loadingRecords = false; // flag to indicate that client is charging a new page of records
        // scroll's watcher to get infinite scrolling
        $(document).scroll(function(e){

            if (!$scope.loadingRecords && !$scope.historyCompleted &&
                ($(window).scrollTop() +  window.innerHeight + 30 >= $(window).height())){

                $scope.loadingRecords = true;
                recordsService.getRecordHistoryList($scope.numPage, function (records) {
                    if (records.length > 0) {
                        $scope.recordHistory = $scope.recordHistory.concat(records);
                        formateDate($scope.recordHistory);
                        sortCustom($scope.recordHistory);
                        $scope.numPage ++;
                        $timeout(function(){
                            $scope.loadingRecords = false;
                        },0,false);
                    } else {
                        $scope.historyCompleted = true;
                    }

                }, showError);
            }
        });

    }]);

