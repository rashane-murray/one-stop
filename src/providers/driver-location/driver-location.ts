import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import { AngularFireDatabase, FirebaseListObservable } from 'angularfire2/database';

/*
  Generated class for the DriverLocationProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class DriverLocationProvider {

  constructor(public http: Http, private afd: AngularFireDatabase) {
  }

  getCoords(){
  	return this.afd.list('/drivers');
  }

}
