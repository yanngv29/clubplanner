'use strict';

describe('Controller Tests', function() {

    describe('ClubEvent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClubEvent, MockSite, MockClub;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClubEvent = jasmine.createSpy('MockClubEvent');
            MockSite = jasmine.createSpy('MockSite');
            MockClub = jasmine.createSpy('MockClub');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClubEvent': MockClubEvent,
                'Site': MockSite,
                'Club': MockClub
            };
            createController = function() {
                $injector.get('$controller')("ClubEventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubplannerApp:clubEventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
