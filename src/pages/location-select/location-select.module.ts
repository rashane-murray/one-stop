import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { LocationSelect } from '../location-select/location-select';

@NgModule({
  declarations: [
    LocationSelectPage,
  ],
  imports: [
    IonicPageModule.forChild(LocationSelectPage),
  ],
  exports: [
    LocationSelectPage
  ]
})
export class LocationSelectPageModule {}
