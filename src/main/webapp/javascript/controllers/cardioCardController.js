angular.module('trainingTrackerApp')

    .controller('cardioCardCtrl', ['$scope', '$state', function ($scope, $state) {

        // function that launches when the plus is clicked and open the add record modal
        $scope.addRecord = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openCardioModal($scope.cardio);
        };
    }]);

