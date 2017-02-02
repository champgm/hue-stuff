$(document).ready(function () {

  $('#lights').on('click', '.btn.light.btn-off.btn-lg', function () {
    toggle(this);
    var lightId = this.id;
    $.get('togglelight', {
      'lightid': lightId
    }, function (resp) {

    }).fail(function () {
      alert('light toggle request failed!');
    })
  })

  $('#lights').on('click', '.btn.light.btn-on.btn-lg', function () {
    toggle(this);
    var lightId = this.id;
    $.get('togglelight', {
      'lightid': lightId
    }, function (resp) {

    }).fail(function () {
      alert('light toggle request failed!');
    })
  })

  $('#scenes').on('click', '.btn.scene.btn-lg', function () {
    $(this).css('color', 'red');
    var sceneId = this.id;
    $.get('activatescene', {
      'sceneid': sceneId
    }, function (resp) {

    }).fail(function () {
      alert('scene activation request failed');
    })
  })

  $('#groups').on('click', '.btn.group.btn-off.btn-lg', function () {
    toggle(this);
    var groupId = this.id;
    $.get('togglegroup', {
      'groupid': groupId
    }, function (resp) {

    }).fail(function () {
      alert('group toggle request failed!');
    })
  })

  $('#groups').on('click', '.btn.group.btn-on.btn-lg', function () {
    toggle(this);
    var groupId = this.id;
    $.get('togglegroup', {
      'groupid': groupId
    }, function (resp) {

    }).fail(function () {
      alert('group toggle request failed!');
    })
  })

  $('#plugs').on('click', '.btn.plug.btn-off.btn-lg', function () {
    toggle(this);
    var plugId = this.id;
    $.get('toggleplug', {
      'plugid': plugId
    }, function (resp) {

    }).fail(function () {
      alert('light toggle request failed!');
    })
  })

  $('#plugs').on('click', '.btn.plug.btn-on.btn-lg', function () {
    toggle(this);
    var plugId = this.id;
    $.get('toggleplug', {
      'plugid': plugId
    }, function (resp) {

    }).fail(function () {
      alert('plug toggle request failed!');
    })
  })

});

function toggle(obj) {
  var offColor = 'rgb(224, 224, 224)'; // light grey
  var onColor = 'rgb(255, 153, 51)'; // orange

  var currentColor = $(obj).css("background-color");

  if (currentColor == onColor) {
    $(obj).css('background-color', offColor);
  } else if (currentColor == offColor) {
    $(obj).css('background-color', onColor);
  }
}