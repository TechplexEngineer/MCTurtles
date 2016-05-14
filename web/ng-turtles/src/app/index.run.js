(function() {
  'use strict';

  angular
    .module('ngTurtles')
    .run(runBlock);

  /** @ngInject */
  function runBlock($log) {

    $log.debug('runBlock end');
  }

})();
