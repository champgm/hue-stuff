import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tab } from './models/tab.model';

@Component({
  moduleId: module.id,
  selector: 'my-app',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private static lights: Tab = new Tab(1, "Lights", "lights");
  private static scenes: Tab = new Tab(2, "Scenes", "scenes").setActive(true);
  private static groups: Tab = new Tab(3, "Groups", "groups");
  private static schedules: Tab = new Tab(4, "Schedules", "schedules");
  private static sensors: Tab = new Tab(5, "Sensors", "sensors");
  private static rules: Tab = new Tab(6, "Rules", "rules");
  private static plugs: Tab = new Tab(7, "Plugs", "plugs");
  public selectedTabTitle: string;
  private tabs: Tab[];

  constructor(private router: Router) { }

  onSelect(tab: Tab): void {
    this.router.navigate([tab.routing]);
  }

  ngOnInit(): void {
    this.getTabs();
    this.selectedTabTitle = 'Scenes';
    this.router.navigate(['scenes']);
  }

  getTabs(): void {
    this.tabs = [
      AppComponent.lights,
      AppComponent.scenes,
      AppComponent.groups,
      AppComponent.schedules,
      AppComponent.sensors,
      AppComponent.rules,
      AppComponent.plugs
    ];
  }
}
