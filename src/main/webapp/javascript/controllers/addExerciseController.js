angular.module('trainingTrackerApp')

    .controller('addExerciseCtrl', ['$scope', '$state', 'exerciseService', function ($scope, $state, exerciseService) {

        $scope.predeterminedExercises = [
            {muscleGroup:"pecho",name:"press banca"},
            {muscleGroup:"espalda",name:"low row"},
            {muscleGroup:"hombro",name:"remo vertical"}
        ];
        $scope.predeterminedMG = [
            "pecho","espalda","hombro"
        ];

        // options selected from the selectors html
        $scope.selectedPredetermined;
        $scope.mgSelected;

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

        $cope.addPredetermined = function () {
            exerciseService.addPredetermined($scope.selectedPredetermined);
        }
    }]);
