package org.bs.oms;

import org.bs.oms.entities.*;
import org.bs.oms.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class OmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmsApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	CommandLineRunner start(AirbaseRepo airbaseRepo, PlaceRepo placeRepo, ParkingRepo parkingRepo, SquadronRepo squadronRepo,
							AircraftMakerRepo aircraftMakerRepo, VersionRepo versionRepo, AircraftRepo aircraftRepo){
		return args -> {

			// AirBases Creation
			Stream.of("DUKHAN AIRBASE","ODEID AIRBASE","DOHA AIRBASE")
					.forEach(a->{
						Airbase airBase = new Airbase();
						airBase.setName(a);
						String[] cit = a.split(" ");
						airBase.setCity(cit[0]);
						airbaseRepo.save(airBase);
					});

			// Places Creation
			for (int i=1; i<=7; i++){
				Place place  = new Place();
				place.setName("F"+i);
				//place.setStatus(i % 2 == 0 ? true : false);
				place.setStatus(true);
				placeRepo.save(place);
			}
			for (int i=1; i<=7; i++){
				Place place  = new Place();
				place.setName("S"+i);
				//place.setStatus(i % 2 == 0 ? true : false);
				place.setStatus(true);
				placeRepo.save(place);
			}

			// Parking Creation
			Stream.of("Parking A","Parking B")
					.forEach(i->{
						Parking parking = new Parking();
						parking.setName(i);
						parking.setColor("Orange");
						parkingRepo.save(parking);
					});

			// Adding Places to Parking
			Parking parkingA = parkingRepo.findById(1L).get();
			Parking parkingB = parkingRepo.findById(2L).get();
			List<Place> placesF = placeRepo.findByNameContaining("F");
			List<Place> placesS = placeRepo.findByNameContaining("S");
			parkingA.setPlaces(placesF);
			parkingRepo.save(parkingA);
			parkingB.setPlaces(placesS);
			parkingRepo.save(parkingB);

			// Adding Parking to Airbases
			Airbase dukhanAirbase = airbaseRepo.findById(1L).get();
			Airbase dohaAirbase = airbaseRepo.findById(3L).get();
			List<Parking> parkings =parkingRepo.findAll();
			dukhanAirbase.setParkings(parkings);
			dohaAirbase.setParkings(parkings);
			airbaseRepo.save(dukhanAirbase);
			airbaseRepo.save(dohaAirbase);

			// Squadrons Creation
			Stream.of("1st SQUADRON","7th SQUADRON","15th SQUADRON","52nd SQUADRON")
					.forEach(a->{
						Squadron squadron  = new Squadron();
						squadron.setName(a);
						squadronRepo.save(squadron);
					});

			// Versions Creation
			Stream.of("RAFALE C","RAFALE B","RAFALE M","TYPHON T1","TYPHON T2","TYPHON T3")
					.forEach(i->{
						Version version = new Version();
						version.setVersion(i);
						version.setDate(new Date());
						versionRepo.save(version);
					});

			// AircraftMakers Creation
			Stream.of("TYPHON","RAFALE","APACHE","F15 EAGLE")
					.forEach(a->{
						AircraftMaker aircraftMaker = new AircraftMaker();
						aircraftMaker.setName(a);
						aircraftMaker.setAirForce(true);
						aircraftMakerRepo.save(aircraftMaker);
					});

			//Adding versions to aircraftMakers
			AircraftMaker typhonAircraftMaker = aircraftMakerRepo.findById(1L).get();
			List<Version> typhonVersions = versionRepo.findByVersionContaining("TYPHON");
			typhonAircraftMaker.setVersions(typhonVersions);
			aircraftMakerRepo.save(typhonAircraftMaker);
			AircraftMaker rafaleAircraftMaker = aircraftMakerRepo.findById(2L).get();
			List<Version> rafaleVersions = versionRepo.findByVersionContaining("RAFALE");
			rafaleAircraftMaker.setVersions(rafaleVersions);
			aircraftMakerRepo.save(rafaleAircraftMaker);

			// Aircraft Creation
			Aircraft aircraft = new Aircraft();
			aircraft.setName("Typhon");
			aircraft.setInterNumber("QA93");
			AircraftMaker maker = aircraftMakerRepo.findById(1L).get();
			aircraft.setAircraftMaker(maker);
			aircraft.setVersion(maker.getVersions().get(0));
			aircraft.setStatus(AircraftStatus.IN_SERVICE);
			aircraft.setAirbase(airbaseRepo.findById(1L).get());
			aircraft.setSquadron(squadronRepo.findById(1L).get());
			aircraftRepo.save(aircraft);

		};
	}

}