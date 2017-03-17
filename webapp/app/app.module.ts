import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TabsModule } from 'ng2-bootstrap';
import { ButtonsModule } from 'ng2-bootstrap';
import { ModalModule } from 'ng2-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { GroupsComponent } from './tabs/groups/groups.component';
import { LightsComponent } from './tabs/lights/lights.component';
import { PlugsComponent } from './tabs/plugs/plugs.component';
import { RulesComponent } from './tabs/rules/rules.component';
import { ScenesComponent } from './tabs/scenes/scenes.component';
import { SchedulesComponent } from './tabs/schedules/schedules.component';
import { SensorsComponent } from './tabs/sensors/sensors.component';

import { AppComponent } from './app.component';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    FormsModule,
    TabsModule.forRoot(),
    ButtonsModule.forRoot(),
    ModalModule.forRoot()
  ],
  declarations: [
    AppComponent,
    GroupsComponent,
    LightsComponent,
    PlugsComponent,
    RulesComponent,
    ScenesComponent,
    SchedulesComponent,
    SensorsComponent
  ],
  providers: [{ provide: LocationStrategy, useClass: HashLocationStrategy }],
  bootstrap: [AppComponent]
})
export class AppModule { }
