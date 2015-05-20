	There are two ways to run your code in Eclipse: using the Ant view, using Run Configurations
	under the run tab.
	
	If you run your code with the Ant view: You may have to alter the following line in the
	build.xml file. Line: 
	<property name="args" value="../input_file_name ../compressed_output_filename ../uncompressed_filename"/>
	So you would specify the input_file_name, compressed_output_filename, and the uncompressed_filename.
	
	If you run your code with Run Configurations: Go to the (x) = Arguments section and specify the
	input_file_name, compressed_output_filename, and the uncompressed_filename.
	
	The "../" in "../input_file_name" assumes your code is one level above the bin directory and in your
	project folder.
	
	We've include a file that you can use to test your code: alice_in_wonderland.txt -- the book.
	
	We strongly urge you to check that your code runs correctly on the command line of the GL server
	after you submit it.