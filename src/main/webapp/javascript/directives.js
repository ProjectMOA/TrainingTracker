angular.module('trainingTrackerApp')

    // include the 'navbar.html' into the <navbar> tag
    .directive('navbar', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navbar.html',
            controller: 'navbarCtrl'
        }
    })

    // include the 'record.html' into the <record> tag
    .directive('record', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/record.html',
            controller: 'recordCtrl',
            scope: true
        }
    })

    //include the 'exerciserCard.html' into the <exerciserCard> tag
    .directive('exerciseCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/exerciseCard.html',
            controller: 'exerciseCardCtrl',
            scope: true
        }
    })

    //include the 'cardioCard.html' into the <cardioCard> tag
    .directive('cardioCard', function () {
        return {
            restrict: 'E',
            templateUrl: 'templates/components/cardioCard.html',
            controller: 'cardioCardCtrl',
            scope: true
        }
    });
