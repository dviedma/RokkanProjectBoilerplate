#compile Sass files
echo "Compiling Sass files..."
compass compile ../ --force

#SSI replacement build
ant build -lib lib/jsch-0.1.42.jar


