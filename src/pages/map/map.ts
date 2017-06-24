import { Component, ViewChild,ElementRef } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';
import { Geolocation } from '@ionic-native/geolocation';

import {  GoogleMaps, GoogleMap, GoogleMapsEvent, LatLng, CameraPosition, MarkerOptions, Marker } from '@ionic-native/google-maps';

declare var google: any;
declare var navigaton:any;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {
  
  @ViewChild('map') mapEle;
  map:any;
  position:any;

  constructor(private googleMaps: GoogleMaps, public navCtrl: NavController, public platform: Platform, public geolocation: Geolocation) { }

  ionViewDidLoad(){
    this.loadMap();
  }

  loadMap() {

    /*let mapEle = this.map.nativeElement;

        if (!mapEle) {
            console.error('Unable to initialize map, no map element with #map view reference.');
            return;
        }*/

    //let TAXI: LatLng = ...
    this.geolocation.getCurrentPosition().then((position)=>{
        let latLng = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);

        let mapOptions = {
            center: latLng, //TAXI
            zoom: 50,
            mapTypeId:google.maps.MapTypeId.ROADMAP

        }

    this.map= new google.maps.Map(this.mapEle.nativeElement,mapOptions);
    },(err) =>{
        console.log(err);
        });
    }

}


