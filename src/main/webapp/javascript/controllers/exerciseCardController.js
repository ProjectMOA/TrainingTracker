angular.module('trainingTrackerApp')

    .controller('exerciseCardCtrl', ['$scope', '$state', 'recordsService', function ($scope, $state, recordsService) {

        $scope.enterExercise = true;

        // function that launches when the plus is clicked and open the add record modal
        $scope.addRecord = function ()  {
            $scope.enterExercise = false;
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openRecordModal($scope.exercise);
        };
        // function that launches when the pencil is clicked and open the modify exercise modal
        $scope.modifyEx = function ()  {
            $scope.enterExercise = false;
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openModifyModal($scope.exercise);
        };
        // function that launches when the plus is clicked and open the delete exercise modal
        $scope.deleteEx = function ()  {
            $scope.enterExercise = false;
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openDeleteModal($scope.exercise);
        };

        $scope.enterHistory = function () {
            if ($scope.enterExercise) {
                recordsService.setExerciseName($scope.exercise.name);
                $state.go('exerciseHistory');
            } else {
                $scope.enterExercise = true;
            }
        }
    }]);
