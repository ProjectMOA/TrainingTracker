angular.module('trainingTrackerApp', ['ui.router', 'base64'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider

            //starter screen
            .state('starter', {
                url: "/starter",
                templateUrl: "templates/starter.html",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated()) {
                        $state.go('home');
                    }
                }
            })
            //sign up screen
            .state('signUp', {
                url: "/signUp",
                templateUrl: "templates/signUp.html",
                controller: "signUpCtrl",
                onEnter: function ($state, auth) {
                    if (auth.isAuthenticated()) {
                        $state.go('home');
                    }
                }
            })
            //home screen
            .state('home', {
                url: "/home",
                templateUrl: "templates/home.html",
                onEnter: function ($state, auth) {
                    if (!auth.isAuthenticated()) {
                        $state.go('starter');
                    }
                }
            });

        $urlRouterProvider.otherwise('starter');
    });
