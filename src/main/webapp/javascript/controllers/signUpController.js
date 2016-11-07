angular.module('trainingTrackerApp')

    .controller('signUpCtrl', ['$scope', '$state', 'auth', function ($scope, $state, auth) {

            // inputs visual variables
            $scope.userName = "";
            $scope.password = "";
            $scope.rePassword = "";
            $scope.email = "";
            $scope.userNameMaxLength = false;
            $scope.passwordMaxLength = false;
            $scope.emailMaxLength = false;

            // Watches to control input variables length
            $scope.$watch('userName', function () {
                if ($scope.userName.length > 50) {
                    $scope.userName = $scope.userName.slice(0, 50);
                } else if ($scope.userName.length == 50) {
                    $scope.userNameMaxLength = true;
                } else {
                    $scope.userNameMaxLength = false;
                }
            });
            $scope.$watch('email', function () {
                if ($scope.email.length > 50) {
                    $scope.email = $scope.email.slice(0, 50);
                } else if ($scope.email.length == 50) {
                    $scope.emailMaxLength = true;
                } else {
                    $scope.emailMaxLength = false;
                }
            });
            $scope.$watch('password', function () {
                if ($scope.password.length > 50) {
                    $scope.password = $scope.password.slice(0, 50);
                } else if ($scope.password.length == 50) {
                    $scope.passwordMaxLength = true;
                } else {
                    $scope.passwordMaxLength = false;
                }
            });

            // feedback handling variables
            $scope.errorMsg = "";
            $scope.error = false;

            // hide the error register message when is true respectively
            $scope.hideError = function () {
                $scope.errorMsg = "";
                $scope.error = false;
            };

            // show the error register message when is false respectively
            var showError = function (error) {
                $scope.errorMsg = error;
                $scope.error = true;
            };

            // send the register form to the auth service
            $scope.signUp = function () {
                // check if the both passwords match
                if ($scope.password !== $scope.rePassword) {
                    showError('Invalid passwords');
                } else {
                    var userObject = {
                        user: $scope.userName,
                        email: $scope.email,
                        pass: $scope.password,
                        repass: $scope.rePassword

                    };
                    auth.register(userObject, showError);
                }
            }
        }]);
