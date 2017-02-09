$(document).ready(() => {

  $('#lights').on('click', '.btn.light.btn-off.btn-lg', function () {
    toggle(this);
    const lightId = this.id;
    $.get('togglelight', {
      lightid: lightId
    }, (resp) => {

    }).fail(() => {
      alert('light toggle request failed!');
    });
  });

  $('#lights').on('click', '.btn.light.btn-on.btn-lg', function () {
    toggle(this);
    const lightId = this.id;
    $.get('togglelight', {
      lightid: lightId
    }, (resp) => {

    }).fail(() => {
      alert('light toggle request failed!');
    });
  });

  $('#scenes').on('click', '.btn.scene.btn-lg', function () {
    $(this).css('color', 'red');
    const sceneId = this.id;
    $.get('activatescene', {
      sceneid: sceneId
    }, (resp) => {

    }).fail(() => {
      alert('scene activation request failed');
    });
  });

  $('#groups').on('click', '.btn.group.btn-off.btn-lg', function () {
    toggle(this);
    const groupId = this.id;
    $.get('togglegroup', {
      groupid: groupId
    }, (resp) => {

    }).fail(() => {
      alert('group toggle request failed!');
    });
  });

  $('#groups').on('click', '.btn.group.btn-on.btn-lg', function () {
    toggle(this);
    const groupId = this.id;
    $.get('togglegroup', {
      groupid: groupId
    }, (resp) => {

    }).fail(() => {
      alert('group toggle request failed!');
    });
  });

  $('#plugs').on('click', '.btn.plug.btn-off.btn-lg', function () {
    toggle(this);
    const plugId = this.id;
    $.get('toggleplug', {
      plugid: plugId
    }, (resp) => {

    }).fail(() => {
      alert('light toggle request failed!');
    });
  });

  $('#plugs').on('click', '.btn.plug.btn-on.btn-lg', function () {
    toggle(this);
    const plugId = this.id;
    $.get('toggleplug', {
      plugid: plugId
    }, (resp) => {

    }).fail(() => {
      alert('plug toggle request failed!');
    });
  });

});

function toggle(obj) {
  const offColor = 'rgb(224, 224, 224)'; // light grey
  const onColor = 'rgb(255, 153, 51)'; // orange

  const currentColor = $(obj).css('background-color');

  if (currentColor == onColor) {
    $(obj).css('background-color', offColor);
  } else if (currentColor == offColor) {
    $(obj).css('background-color', onColor);
  }
}
