apt-get install git #install git
apt-get install libssl-dev
add-apt-repository ppa:webupd8team/java
apt-get update
apt-get install oracle-java7-installer #install jdk 1.7
sudo apt-get install mongodb #install mongodb
wget https://bintray.com/artifact/download/vertx/downloads/vert.x-2.1.5.tar.gz #download vert.x 2.1.5
tar -zxvf vert.x-2.1.5.tar.gz $home #unzip jdk 1.7
apt-get install r-base r-cran-rserve  #install R
rm -r Kocom
git clone https://github.com/yhk1030/Kocom.git #clone the kocom repository
mv Kocom/Kocom/hivemq-2.3.1 hivemq-2.3.1 #move hivemq(mqtt broker)
mv Kocom/Kocom/R_Pkgs/* ~
R CMD BATCH installpkgs1.R
apt-get install r-cran-rjava
apt-get install liblzma-dev
R CMD javareconf
R CMD BATCH installpkgs2.R log
R CMD Rserve --no-save --RS-enable-remote &
mv Kocom/Kocom/vertx-modules .vertx-modules  #move vert.x module

sed -i '$s/$/\nVERTX_HOME=$HOME\/vert.x-2.1.5\nPATH=$VERTX_HOME\/bin:$PATH\/\nVERTX_MODS=$HOME\/.vertx-modules\/ /' /etc/environment #add path
mv Kocom/Kocom/app.js app.js #make app.js(vertx) file
sed -i '$s/.*/mongod --fork --logpath \/home\/shicnova\/db\/log\/log.log --logappend --port 30000 --dbpath \/home\/shicnova\/db\/data /' /etc/rc.local
sed -i '$s/$/\n\/home\/shicnova\/hivemq-2.3.1\/bin\/run.sh \& /' /etc/rc.local
sed -i '$s/$/\nvertx run \/home\/shicnova\/app.js \&\nexit 0 /' /etc/rc.local
 #add run command for start
mkdir db
mkdir db/log
mkdir db/data

mongod --fork --logpath db/log/log.log --logappend --port 30000 --dbpath db/data/  #Run mongodb
./hivemq-2.3.1/bin/run.sh & #Run hivemq
vertx run app.js & #Run server
