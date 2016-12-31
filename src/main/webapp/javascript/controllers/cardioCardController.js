angular.module('trainingTrackerApp')

    .controller('cardioCardCtrl', ['$scope', '$state', 'recordsService', function ($scope, $state, recordsService) {

        // function that launches when the plus is clicked and open the add record modal
        $scope.addRecord = function ()  {
            $scope.hideSuccess();
            $scope.hideError();
            $scope.openRecordModal($scope.cardio);
        };
    }]);

