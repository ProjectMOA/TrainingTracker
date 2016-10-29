angular.module('trainingTrackerApp')

    .controller('homeCtrl', ['$scope', '$state', 'exerciseService',
        function ($scope, $state, exerciseService) {

            $scope.exercisesList = []; //exercise list
            // exercise list server request
            $scope.getExercisesList = function () {
                exerciseService.getExercisesList(function (exercises) {
                    $scope.exercisesList = exercises;
                },showError);
            };
            $scope.getExercisesList();

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
