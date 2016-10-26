angular.module('trainingTrackerApp')

    // include the 'navbar.html' into the <navbar> tag
    .directive('navbar', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navbar.html',
            controller: 'navbarCtrl'
        }
    })

    //include the 'exerciserCard.html' into the <exerciserCard> tag
    .directive('exerciseCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/exerciseCard.html',
            controller: 'exerciseCardController',
            scope: true
        }
    });
