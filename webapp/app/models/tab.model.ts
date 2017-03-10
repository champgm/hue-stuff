export class Tab {
  private active: boolean = false;

  constructor(
    private _id: number = -1,
    public title?: String,
    public routing?: String) {
  }

  setActive(active: boolean): Tab {
    this.active = active;
    return this;
  }

  isActive() {
    return this.active;
  }
}