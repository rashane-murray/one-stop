import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { Items } from '../../providers/providers';

@Component({
  selector: 'trip',
  templateUrl: 'trip-detail.html'
})
export class TripPage {
  trip: any;

  constructor(public navCtrl: NavController, navParams: NavParams, items: Items) {
    this.trip = navParams.get('item') || items.defaultItem;
  }



}
