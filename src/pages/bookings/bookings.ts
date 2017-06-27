import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'bookings',
  templateUrl: 'bookings.html'
})
export class BookingsPage {
  drivers: any[];

  constructor(public navCtrl: NavController) {
    this.drivers = [
      {
        user: {
          avatar: 'assets/img/marty-avatar.png',
          name: 'Marty McFly'
        },
        ratings: '4-stars',
        content: ''
      },
      {
        user: {
          avatar: 'assets/img/sarah-avatar.png.jpeg',
          name: 'Sarah Connor'
        },
        ratings: '5-stars',
        image: 'assets/img/advance-card-tmntr.jpg',
        content: ''
      },
      {
        user: {
          avatar: 'assets/img/ian-avatar.png',
          name: 'Ian Malcolm'
        },
        ratings: '3 1/2-stars',
        content: ''
      }
    ];

  }
}
