import { Component } from '@angular/core';
import { OnInit } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'my-app',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  tabs: string[];
  selectedTab: string = "Scenes";

  onSelect(tab: string): void {
    this.selectedTab = tab;
  }

  ngOnInit(): void {
    this.getTabs();
  }

  getTabs(): void {
    this.tabs = ["Lights", "Scenes", "Groups", "Schedules", "Sensors", "Plugs"];
  }
}
