

class UtilityScenes {
  static getAllUtilitySceneIds() {
    return [UtilityScenes.getAllOffId()];
  }

  static getAllUtilityScenes() {
    const allUtilityScenes = {};

    allUtilityScenes[UtilityScenes.getAllOffId()] = UtilityScenes.getAllOffScene();

    return allUtilityScenes;
  }

  static getAllOffId() {
    return 'ee29c141-c738-44ec-8863-c4feff3741b7';
  }

  static getAllOffScene() {
    const allOffScene = {
      name: 'ALL OFF',
      id: UtilityScenes.getAllOffId()
    };
    return allOffScene;
  }
}

module.exports = UtilityScenes;
