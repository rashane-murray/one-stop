import { Component, ViewChild } from '@angular/core';
import { NavController, Platform } from 'ionic-angular';

import {  GoogleMaps, GoogleMap, GoogleMapsEvent, LatLng, CameraPosition, MarkerOptions, Marker } from '@ionic-native/google-maps';

declare var google: any;

@Component({
  selector: 'page-map',
  templateUrl: 'map.html'
})
export class MapPage {
  
  @ViewChild('map') map;

  constructor(private googleMaps: GoogleMaps, public navCtrl: NavController, public platform: Platform) { }


  ngAfterViewInit() {
    this.ionViewDidLoad();
  }

  loadMap() {


    // create a new map by passing HTMLElement
    let element: HTMLElement = document.getElementById('map');
    let map: GoogleMap = this.googleMaps.create(element);

    // listen to MAP_READY event
    // You must wait for this event to fire before adding something to the map or modifying it in anyway
    
    map.one(GoogleMapsEvent.MAP_READY).then(() => {
        console.log('Map is ready!');
        }
    );

    // create LatLng object
    let ionic: LatLng = new LatLng(43.0741904, -89.3809802);

    // create CameraPosition
    let position: CameraPosition = {
      target: ionic,
      zoom: 18,
      tilt: 30
    };

    // move the map's camera to position
    map.moveCamera(position);

    // create new marker
    let markerOptions: MarkerOptions = {
        position: ionic,
        title: 'Ionic'
    };

    map.addMarker(markerOptions).then((marker: Marker) => {
        marker.showInfoWindow();
        });
    }

    initJSMaps(mapEle) {
        new google.maps.Map(mapEle, {
            center: { lat: 43.071584, lng: -89.380120 },
            zoom: 16
        });
    }

    initNativeMaps(mapEle) {
        this.map = new GoogleMap(mapEle);
        mapEle.classList.add('show-map');

        this.map.isAvailable().then(() => {
            const position = new LatLng(43.074395, -89.381056);
            this.map.setPosition(position);
        });

    }

    ionViewDidLoad() {
        let mapEle = this.map.nativeElement;

        if (!mapEle) {
            console.error('Unable to initialize map, no map element with #map view reference.');
            return;
    }
        this.loadMap();
        /**
        *   Disable this switch if you'd like to only use JS maps, as the APIs
        *   are slightly different between the two. However, this makes it easy
        *   to use native maps while running in Cordova, and JS maps on the web.
        */
        if (this.platform.is('cordova') === true) {
            this.initNativeMaps(mapEle);
        } else { 
            this.initJSMaps(mapEle);
        } 

    } 

}
