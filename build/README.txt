For SSI replacement:
1. copy .jar files in the 'lib' folder to /usr/share/ant/lib
2. create your dsa pair of keys: http://docs.oracle.com/cd/E19253-01/816-4557/sshuser-33/index.html
	2.1. copy the content of your id_dsa.pub into /home/{username}/.ssh/authorized_keys on the 'lamp' server
	2.2. make sure 'lamp.rokkan.com' is listed in your ~/.ssh/known_hosts
3. run ./publish.sh from the 'build' folder


// * old publish.sh
// #compile Sass files
// echo "Compiling Sass files..."
// compass compile ../ --force

// #SSI replacement build
// ant build -lib lib/jsch-0.1.42.jar

