angular.module('trainingTrackerApp')

    .controller('homeCtrl', ['$scope', '$state', 'exerciseService',
        function ($scope, $state, exerciseService) {

            $scope.exerciseList = []; //exercise list
            // exercise list server request
            $scope.getExerciseList = function () {
                exerciseService.getExerciseList(function (exercises) {
                    $scope.exerciseList = exercises;
                });
            };
            $scope.getExerciseList();

            // feedback handling variables
            $scope.error = false;
            $scope.success = false;
            $scope.successMsg = "";
            $scope.errorMsg = "";

            // hide the error mensage
            $scope.hideError = function () {
                $scope.errorMsg = "";
                $scope.error = false;
            };
            // show the error mensage
            var showError = function (error) {
                $scope.errorMsg = error;
                $scope.error = true;
            };

            // show the success mensage
            var showSuccess = function (message) {
                $scope.successMsg = message;
                $scope.success = true;
            };

            // hide the success mensage
            $scope.hideSuccess = function () {
                $scope.success = false;
                $scope.successMsg = "";
            };

        }]);
