import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Http } from '@angular/http';
import { ItemsComponent } from '../common/items.component';

import 'rxjs/add/operator/toPromise';

@Component({
  // moduleId: module.id,
  selector: 'schedules',
  templateUrl: '../common/items.component.html',
})
export class SchedulesComponent extends ItemsComponent implements OnInit {
  itemType: string = 'schedules';

  constructor(http: Http) {
    super(http);
  }
}
