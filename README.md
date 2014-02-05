MQDumper
========

Simple tool to dump the content of a WebSphere MQ


Sample command line:
java -jar MQBrowser.jar -h 1.2.4.5 -p 1414 -m "TESTMGN" -c "CHAN" -q "Q1,Q2" --username "" --password "" --outdir "./outdir"

Output would be:

Outdir/Q1-mqid.bin
Outdir/Q1-mqid.zip
Outdir/Q2-mqid.xml

