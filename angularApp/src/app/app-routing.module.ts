import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GroupsComponent } from './tabs/groups/groups.component';
import { PlugsComponent } from './tabs/plugs/plugs.component';
import { SchedulesComponent } from './tabs/schedules/schedules.component';
import { SensorsComponent } from './tabs/sensors/sensors.component';
import { ScenesComponent } from './tabs/scenes/scenes.component';
import { LightsComponent } from './tabs/lights/lights.component';
import { AppComponent } from './app.component';

const routes: Routes = [
  {
    path: 'groups',
    component: GroupsComponent
  },
  {
    path: 'lights',
    component: LightsComponent
  },
  {
    path: 'plugs',
    component: PlugsComponent
  },
  {
    path: 'scenes',
    component: ScenesComponent
  },
  {
    path: 'schedules',
    component: SchedulesComponent
  },
  {
    path: 'sensors',
    component: SensorsComponent
  },
  {
    path: 'app',
    component: AppComponent
  }
  // {
  //   path: '',
  //   redirectTo: '/app',
  //   pathMatch: 'full'
  // }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
