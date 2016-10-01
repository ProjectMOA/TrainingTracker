angular.module('trainingTrackerApp')

    // include the 'navbar.html' into the <navbar> tag
    .directive('navbar', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navbar.html',
            controller: 'navbarController'
        }
    })

    //include the 'exerciserCard.html' into the <exerciserCard> tag
    .directive('exerciserCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/exerciserCard.html',
            controller: 'exerciserCardController',
            scope: true
        }
    });
