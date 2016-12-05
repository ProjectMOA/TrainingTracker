angular.module('trainingTrackerApp')

    .controller('exerciseCardCtrl', ['$scope', '$state', function ($scope, $state) {

        // function that launches when the plus is clicked and open the add record modal
        $scope.addRecord = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openRecordModal($scope.exercise);
        };
        // function that launches when the pencil is clicked and open the modify exercise modal
        $scope.modifyEx = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openModifyModal($scope.exercise);
        };
        // function that launches when the plus is clicked and open the delete exercise modal
        $scope.deleteEx = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openDeleteModal($scope.exercise);
        }

    }]);
