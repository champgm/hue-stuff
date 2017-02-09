$(document).ready(() => {

  $('a[data-toggle="tab"]').on('shown.bs.tab', (e) => {
    const target = $(e.target).attr('href');
    if (target == '#lights') {
      $.ajax({
        url: 'getlights',
        method: 'GET',
        beforeSend() {
          $('#lights').empty();
          $('#lights').append('</br><div id="loading_lights" class="loading" style="display:none;">' +
            'Getting lights. Please wait....<br>' +
            '</div>');
          $('#loading_lights').show();
        },
        success(resp) {
          printLights(resp);
        },
        error() {
          alert('retrieve lights request failed.');
        }
      });
    } else if (target == '#scenes') {
      $.ajax({
        url: 'getscenes',
        method: 'GET',
        data: {
          v2: true
        },
        beforeSend() {
          $('#scenes').empty();
          $('#scenes').append('</br><div id="loading_scenes" class="loading" style="display:none;">' +
            'Getting scenes. Please wait....<br>' +
            '</div>');
          $('#loading_scenes').show();
        },
        success(resp) {
          printScenes(resp);
        },
        error() {
          alert('retrieve scenes request failed.');
        }
      });
    } else if (target == '#groups') {
      $.ajax({
        url: 'getgroups',
        method: 'GET',
        beforeSend() {
          $('#groups').empty();
          $('#groups').append('</br><div id="loading_groups" class="loading" style="display:none;">' +
            'Getting groups. Please wait....<br>' +
            '</div>');
          $('#loading_groups').show();
        },
        success(resp) {
          printGroups(resp);
        },
        error() {
          alert('retrieve groups request failed.');
        }
      });
    } else if (target == '#schedules') {
      $.ajax({
        url: 'getschedules',
        method: 'GET',
        beforeSend() {
          $('#schedules').empty();
          $('#schedules').append('</br><div id="loading_schedules" class="loading" style="display:none;">' +
            'Getting schedules. Please wait....<br>' +
            '</div>');
          $('#loading_schedules').show();
        },
        success(resp) {
          printSchedules(resp);
        },
        error() {
          alert('retrieve schedules request failed.');
        }
      });
    } else if (target == '#sensors') {
      $.ajax({
        url: 'getsensors',
        method: 'GET',
        beforeSend() {
          $('#sensors').empty();
          $('#sensors').append('</br><div id="loading_sensors" class="loading" style="display:none;">' +
            'Getting sensors. Please wait....<br>' +
            '</div>');
          $('#loading_sensors').show();
        },
        success(resp) {
          printSensors(resp);
        },
        error() {
          alert('retrieve sensors request failed.');
        }
      });
    } else if (target == '#plugs') {
      $.ajax({
        url: 'getplugs',
        method: 'GET',
        beforeSend() {
          $('#plugs').empty();
          $('#plugs').append('</br><div id="loading_plugs" class="loading" style="display:none;">' +
            'Getting plugs. Please wait....<br>' +
            '</div>');
          $('#loading_plugs').show();
        },
        success(resp) {
          printPlugs(resp);
        },
        error() {
          alert('retrieve plugs request failed.');
        }
      });
    }
  });
});
