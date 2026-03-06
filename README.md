com.mps.birds.core -> HTTP clients to communicate with external web service
com.mps.birds.ui -> UI for plugin
com.mps.birds.jackson -> internal plugin with Jackson dependencies
Birds-ws -> web service
BirdsTestTask.zip -> plugin

Birds service start and plugin installation:
1. Build birds-ws project
2. Build image by running docker build -t birds-ws .
3. Run docker-compose up command to start DB and service containers
4. You can check if application is up checking http://localhost:8080/swagger-ui/index.html#
5. Install plugin (BirdsTestTask.zip) in eclipse
6. Search for the view with name "Birds And Sightings"

Plugin functionality:
1.You can insert birds name, color, weight (double), height (double) and press button "Add Bird" to save new bird.
2.It will automatically be added to the list of all birds in table below.
3.You can refresh list of all birds by pressing "Show all birds" button.
4.When choosing any birds from "Birds list" it automatically fetches all location for that bird.
5.You can add new sighting for the bird by adding it in "Location" input, adding date and time and pressing "Add sighting.." button
6.If input "Bird Name" is empty and "Filter By Bird Name" button is pressed, all sightings will be fetched and populated in table below.
7.If you want to filter by some name, just fill the Bird name input and press previous button again.
8.You won't be able to add sighting for bird which is not in DB
8.You won't be able to add sighting for future date
8.You won't be able to add bird without name, color, weight or height