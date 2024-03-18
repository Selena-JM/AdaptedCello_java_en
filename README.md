## Changes for it to compile properly
Supposed to work without installing anything at [www.cellocad.org](http://www.cellocad.org/) but does not so had to install and as a messssss

- Go to https://github.com/CIDARLAB/cello/blob/develop/INSTALL.md for information on how to install
- The repository used https://repo.spring.io/libs-release has since become private so we cannot access what is in this repository
    - Can still name it in the pom.xml file as a repository but all dependencies need to be installed manually
    - Follow the installation without docker (installation of maven and then installation of the local_jars)
    - I wanted to still use docker so I then got back to the installation with docker : See https://stackoverflow.com/questions/55183286/add-local-external-jar-should-not-point-at-files-within-the-project-directory for how to write the dependencies after having installed the jar files. Need to change the following according to the local installation of the jars and not from the repository :
    1. pom.xml file 
    2. dockerfile 
    3. The installation file install_local_jars.sh (The mvn compilation did not work without the  `-DcreateChecksum=true` in the installation file)
- The java code calls packages that do not exist : maybe it is me who is not able to properly handle the jar file, but to solve this issue I found this issue https://github.com/CIDARLAB/cello/issues/40 that was exactly my problem and so I found the source code https://github.com/CIDARLAB/NetlistSynthesizer and manually added the needed packages
    
    /cello/src/main/java/org/cellocad/BU/
    
    /cello/src/main/java/org/cellocad/MIT/heuristicsearch/
    
    /cello/src/main/java/org/cellocad/MIT/dnacompiler/BGate
    
    /cello/src/main/java/org/cellocad/MIT/dnacompiler/BNode
    
    com.fasterxml.uuid —> I added this one thanks to the dependency declaration in the pom.xml file : 
    
    ```
    <dependency>
        <groupId>com.fasterxml.uuid</groupId>
        <artifactId>java-uuid-generator</artifactId>
        <version>5.0.0</version>
    </dependency>
    ```
    

—> after that the build was successful 

- Need to change the version of lombok package otherwise we get the error Illegal reflective access by lombok.javac.apt.LombokProcessor
    
    version 1.18.12 works fine https://github.com/projectlombok/lombok/issues/1962
    
- Need to delete the spring security dependency from pom.xml file otherwise when launching the application there will be this pop up
    
    ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/652f0cd2-12cf-4146-a485-bcc184fc174b/b6fd38cf-6fda-4c71-89cd-9b4a86ed0aa8/Untitled.png)
    
    https://stackoverflow.com/questions/45232071/springboot-401-unauthorized-even-with-out-security
    

Had to modify BaseController at line 35 from i = 0 to i=2 so that the C: in windows path becomes only a / and fits the unix path type, otherwise couldn’t access the files in cello_results/SelenaJM/ which are the inputs and outputs

—> The interface works and can enter the input and outputs but nothing runs

- Need to enter the code to make the run button work or need to only do command line runs ?
    - When clicking on run nothing happens
    - When running in the command line interface, get the errors :
    
    ```
    mvn -f ../pom.xml -DskipTests=true -PCelloMain -Dexec.args="-verilog demo_verilog.v -input_promoters demo_inputs.txt -output_genes demo_outputs.txt"
    
    ```
    
    ```jsx
    org.cellocad.BU.netsynth.Utilities getFileContentAsStringList
    SEVERE: null
    
    java.io.FileNotFoundException: job_1709738047825\netSynth\abcOutput.bench (Le fichier sp▒cifi▒ est introuvable)
            at java.base/java.io.FileInputStream.open0(Native Method)
            at java.base/java.io.FileInputStream.open(FileInputStream.java:219)
            at java.base/java.io.FileInputStream.<init>(FileInputStream.java:157)
            at java.base/java.io.FileReader.<init>(FileReader.java:75)
            at org.cellocad.BU.netsynth.Utilities.getFileContentAsStringList(Utilities.java:230)
            at org.cellocad.BU.adaptors.ABCAdaptor.convertBenchToAIG(ABCAdaptor.java:177)
            at org.cellocad.BU.adaptors.ABCAdaptor.runABC(ABCAdaptor.java:153)
            at org.cellocad.BU.netsynth.NetSynth.runEspressoAndABC(NetSynth.java:990)
            at org.cellocad.BU.netsynth.NetSynth.getNetlistCode(NetSynth.java:679)
            at org.cellocad.BU.netsynth.NetSynth.getNetlist(NetSynth.java:560)
            at org.cellocad.BU.netsynth.NetSynth.runNetSynth(NetSynth.java:511)
            at org.cellocad.BU.netsynth.NetSynth.runNetSynth(NetSynth.java:497)
            at org.cellocad.MIT.dnacompiler.DNACompiler.getAbstractCircuit(DNACompiler.java:1722)
            at org.cellocad.MIT.dnacompiler.DNACompiler.run(DNACompiler.java:235)
            at org.cellocad.MIT.dnacompiler.CelloMain.main(CelloMain.java:15)
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
            at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
            at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
            at java.base/java.lang.reflect.Method.invoke(Method.java:566)
            at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:293)
            at java.base/java.lang.Thread.run(Thread.java:834)
    
    mars 06, 2024 4:14:09 PM org.cellocad.BU.netsynth.Utilities getFileContentAsStringList
    SEVERE: null
    [WARNING]
    java.lang.reflect.InvocationTargetException
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:62)
        at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke (Method.java:566)
        at org.codehaus.mojo.exec.ExecJavaMojo$1.run (ExecJavaMojo.java:293)
        at java.lang.Thread.run (Thread.java:834)
    Caused by: java.lang.IllegalStateException: Error in abstract circuit.  Exiting.
        at org.cellocad.MIT.dnacompiler.DNACompiler.run (DNACompiler.java:237)
        at org.cellocad.MIT.dnacompiler.CelloMain.main (CelloMain.java:15)
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
        at jdk.internal.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:62)
        at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke (Method.java:566)
        at org.codehaus.mojo.exec.ExecJavaMojo$1.run (ExecJavaMojo.java:293)
        at java.lang.Thread.run (Thread.java:834)
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD FAILURE
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  40.472 s
    [INFO] Finished at: 2024-03-06T16:14:09+01:00
    [INFO] ------------------------------------------------------------------------
    [ERROR] Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.4.0:java (default) on project cellocad: An exception occured while executing the Java class. null: InvocationTargetException: Error in abstract circuit.  Exiting. -> [Help 1]
    [ERROR]
    [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
    [ERROR] Re-run Maven using the -X switch to enable full debug logging.
    [ERROR]
    [ERROR] For more information about the errors and possible solutions, please read the following articles:
    [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoExecutionException
    ```
    
    - Trying the python CLI :
    - Doesn’t work : permission denied
        
        ```jsx
        pip install --editable .
        Obtaining file:///C:/Users/S%C3%A9l%C3%A9na/Cello/cello/tools/pycello
        Preparing metadata ([setup.py](http://setup.py/)) ... done
        Requirement already satisfied: Click in c:\python311\lib\site-packages (from cello==0.1) (8.1.7)
        Requirement already satisfied: requests in c:\users\séléna\appdata\roaming\python\python311\site-packages (from cello==0.1) (2.31.0)
        Requirement already satisfied: colorama in c:\users\séléna\appdata\roaming\python\python311\site-packages (from Click->cello==0.1) (0.4.6)
        Requirement already satisfied: charset-normalizer<4,>=2 in c:\users\séléna\appdata\roaming\python\python311\site-packages (from requests->cello==0.1) (3.3.2)
        Requirement already satisfied: idna<4,>=2.5 in c:\users\séléna\appdata\roaming\python\python311\site-packages (from requests->cello==0.1) (3.6)
        Requirement already satisfied: urllib3<3,>=1.21.1 in c:\python311\lib\site-packages (from requests->cello==0.1) (2.0.7)
        ```
        
        ```jsx
        Requirement already satisfied: certifi>=2017.4.17 in c:\users\séléna\appdata\roaming\python\python311\site-packages (from requests->cello==0.1) (2024.2.2)
        Installing collected packages: cello
        Attempting uninstall: cello
        Found existing installation: cello 0.1
        Uninstalling cello-0.1:
        Successfully uninstalled cello-0.1
        Running [setup.py](http://setup.py/) develop for cello
        error: subprocess-exited-with-error
        ```
        
        ```
        × python setup.py develop did not run successfully.
        │ exit code: 1
        ╰─> [18 lines of output]
            running develop
            C:\\Python311\\Lib\\site-packages\\setuptools\\command\\easy_install.py:144: EasyInstallDeprecationWarning: easy_install command is deprecated. Use build and pip and other standards-based tools.
              warnings.warn(
            C:\\Python311\\Lib\\site-packages\\setuptools\\command\\install.py:34: SetuptoolsDeprecationWarning: setup.py install is deprecated. Use build and pip and other standards-based tools.
              warnings.warn(
            running egg_info
            writing cello.egg-info\\PKG-INFO
            writing dependency_links to cello.egg-info\\dependency_links.txt
            writing entry points to cello.egg-info\\entry_points.txt
            writing requirements to cello.egg-info\\requires.txt
            writing top-level names to cello.egg-info\\top_level.txt
            reading manifest file 'cello.egg-info\\SOURCES.txt'
            writing manifest file 'cello.egg-info\\SOURCES.txt'
            running build_ext
            Creating c:\\python311\\lib\\site-packages\\cello.egg-link (link to .)
            cello 0.1 is already the active version in easy-install.pth
            Installing cello-script.py script to C:\\Python311\\Scripts
            error: [Errno 13] Permission denied: 'C:\\\\Python311\\\\Scripts\\\\cello-script.py'
            [end of output]
        
        note: This error originates from a subprocess, and is likely not a problem with pip.
        
        ```
        
        ```jsx
        Rolling back uninstall of cello
        Moving to c:\python311\lib\site-packages\cello.egg-link
        from C:\Users\Séléna\AppData\Local\Temp\pip-uninstall-vwnn21gt\cello.egg-link
        error: subprocess-exited-with-error
        ```
        
        ```jsx
        × python [setup.py](http://setup.py/) develop did not run successfully.
        │ exit code: 1
        ╰─> [18 lines of output]
        running develop
        C:\Python311\Lib\site-packages\setuptools\command\easy_install.py:144: EasyInstallDeprecationWarning: easy_install command is deprecated. Use build and pip and other standards-based tools.
        warnings.warn(
        C:\Python311\Lib\site-packages\setuptools\command\[install.py:34](http://install.py:34/): SetuptoolsDeprecationWarning: [setup.py](http://setup.py/) install is deprecated. Use build and pip and other standards-based tools.
        warnings.warn(
        running egg_info
        writing cello.egg-info\PKG-INFO
        writing dependency_links to cello.egg-info\dependency_links.txt
        writing entry points to cello.egg-info\entry_points.txt
        writing requirements to cello.egg-info\requires.txt
        writing top-level names to cello.egg-info\top_level.txt
        reading manifest file 'cello.egg-info\SOURCES.txt'
        writing manifest file 'cello.egg-info\SOURCES.txt'
        running build_ext
        Creating c:\python311\lib\site-packages\cello.egg-link (link to .)
        cello 0.1 is already the active version in easy-install.pth
        Installing [cello-script.py](http://cello-script.py/) script to C:\Python311\Scripts
        error: [Errno 13] Permission denied: 'C:\\Python311\\Scripts\\[cello-script.py](http://cello-script.py/)'
        [end of output]
        ```
        
        ```jsx
        note: This error originates from a subprocess, and is likely not a problem with pip.
        ```
        
    - Same with another call
        
        ```jsx
        python cello_client.py submit --jobid "job_1709804965351" --verilog "C:/Users/Séléna/Cello/cello/demo/demo_verilog.v" --inputs "C:/Users/Séléna/Cello/cello/demo/demo_inputs.txt" --outputs "C:/Users/Séléna/Cello/cello/demo/demo_outputs.txt"
        ```
        
        ```jsx
        Traceback (most recent call last):
        File "C:\Python311\Lib\site-packages\urllib3\[connection.py](http://connection.py/)", line 203, in _new_conn
        sock = connection.create_connection(
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\urllib3\util\[connection.py](http://connection.py/)", line 85, in create_connection
        raise err
        File "C:\Python311\Lib\site-packages\urllib3\util\[connection.py](http://connection.py/)", line 73, in create_connection
        sock.connect(sa)
        ConnectionRefusedError: [WinError 10061] Aucune connexion n’a pu être établie car l’ordinateur cible l’a expressément refusée
        ```
        
        ```jsx
        The above exception was the direct cause of the following exception:
        ```
        
        ```jsx
        Traceback (most recent call last):
        File "C:\Python311\Lib\site-packages\urllib3\[connectionpool.py](http://connectionpool.py/)", line 791, in urlopen
        response = self._make_request(
        ^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\urllib3\[connectionpool.py](http://connectionpool.py/)", line 497, in _make_request
        conn.request(
        File "C:\Python311\Lib\site-packages\urllib3\[connection.py](http://connection.py/)", line 395, in request
        self.endheaders()
        File "C:\Python311\Lib\http\[client.py](http://client.py/)", line 1277, in endheaders
        self._send_output(message_body, encode_chunked=encode_chunked)
        File "C:\Python311\Lib\http\[client.py](http://client.py/)", line 1037, in _send_output
        self.send(msg)
        File "C:\Python311\Lib\http\[client.py](http://client.py/)", line 975, in send
        self.connect()
        File "C:\Python311\Lib\site-packages\urllib3\[connection.py](http://connection.py/)", line 243, in connect
        self.sock = self._new_conn()
        ^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\urllib3\[connection.py](http://connection.py/)", line 218, in _new_conn
        raise NewConnectionError(
        urllib3.exceptions.NewConnectionError: <urllib3.connection.HTTPConnection object at 0x0000029CEFCF7610>: Failed to establish a new connection: [WinError 10061] Aucune connexion n’a pu être établie car l’ordinateur cible l’a expressément refusée
        ```
        
        ```jsx
        The above exception was the direct cause of the following exception:
        ```
        
        ```jsx
        Traceback (most recent call last):
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[adapters.py](http://adapters.py/)", line 486, in send
        resp = conn.urlopen(
        ^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\urllib3\[connectionpool.py](http://connectionpool.py/)", line 845, in urlopen
        retries = retries.increment(
        ^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\urllib3\util\[retry.py](http://retry.py/)", line 515, in increment
        raise MaxRetryError(_pool, url, reason) from reason  # type: ignore[arg-type]
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        urllib3.exceptions.MaxRetryError: HTTPConnectionPool(host='127.0.0.1', port=8080): Max retries exceeded with url: /submit?id=job_1709804965351&input_promoter_data=pBAD+0.0082+2.5+ACTTTTCATACTCCCGCCATTCAGAGAAGAAACCAATTGTCCATATTGCATCAGACATTGCCGTCACTGCGTCTTTTACTGGCTCTTCTCGCTAACCAAACCGGTAACCCCGCTTATTAAAAGCATTCTGTAACAAAGCGGGACCAAAGCCATGACAAAAACGCGTAACAAAAGTGTCTATAATCACGGCAGAAAAGTCCACATTGATTATTTGCACGGCGTCACACTTTGCTATGCCATAGCATTTTTATCCATAAGATTAGCGGATCCTACCTGACGCTTTTTATCGCAACTCTCTACTGTTTCTCCATACCCGTTTTTTTGGGCTAGC%0ApTac+0.0034+2.8+AACGATCGTTGGCTGTGTTGACAATTAATCATCGGCTCGTATAATGTGTGGAATTGTGAGCGCTCACAATT%0A&output_gene_data=RFP+CTGAAGTGGTCGTGATCTGAAACTCGATCACCTGATGAGCTCAAGGCAGAGCGAAACCACCTCTACAAATAATTTTGTTTAATACTAGAGTCACACAGGAAAGTACTAGATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAACAGATAAAAAAAATCCTTAGCTTTCGCTAAGGATGATTTCT%0A&verilog_text=module+A%28output+out1%2C++input+in1%2C+in2%29%3B%0A++always%40%28in1%2Cin2%29%0A++++begin%0A++++++case%28%7Bin1%2Cin2%7D%29%0A++++++++2%27b00%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b01%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b10%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b11%3A+%7Bout1%7D+%3D+1%27b1%3B%0A++++++endcase%0A++++end%0Aendmodule%0A (Caused by NewConnectionError('<urllib3.connection.HTTPConnection object at 0x0000029CEFCF7610>: Failed to establish a new connection: [WinError 10061] Aucune connexion n’a pu être établie car l’ordinateur cible l’a expressément refusée'))
        ```
        
        ```jsx
        During handling of the above exception, another exception occurred:
        ```
        
        ```jsx
        Traceback (most recent call last):
        File "C:\Users\Séléna\Cello\cello\tools\pycello\cello_client.py", line 348, in <module>
        cli()
        File "C:\Python311\Lib\site-packages\click\[core.py](http://core.py/)", line 1157, in **call**
        return self.main(*args, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\click\[core.py](http://core.py/)", line 1078, in main
        rv = self.invoke(ctx)
        ^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\click\[core.py](http://core.py/)", line 1688, in invoke
        return _process_result(sub_ctx.command.invoke(sub_ctx))
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\click\[core.py](http://core.py/)", line 1434, in invoke
        return ctx.invoke(self.callback, **ctx.params)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\click\[core.py](http://core.py/)", line 783, in invoke
        return __callback(*args, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Python311\Lib\site-packages\click\[decorators.py](http://decorators.py/)", line 33, in new_func
        return f(get_current_context(), *args, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\Cello\cello\tools\pycello\cello_client.py", line 197, in submit
        r = requests.post(endpoint, params=params, auth=ctx.obj.auth)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[api.py](http://api.py/)", line 115, in post
        return request("post", url, data=data, json=json, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[api.py](http://api.py/)", line 59, in request
        return session.request(method=method, url=url, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[sessions.py](http://sessions.py/)", line 589, in request
        ```
        
        ```jsx
        resp = self.send(prep, **send_kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[sessions.py](http://sessions.py/)", line 703, in send
        r = adapter.send(request, **kwargs)
        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        File "C:\Users\Séléna\AppData\Roaming\Python\Python311\site-packages\requests\[adapters.py](http://adapters.py/)", line 519, in send
        raise ConnectionError(e, request=request)
        requests.exceptions.ConnectionError: HTTPConnectionPool(host='127.0.0.1', port=8080): Max retries exceeded with url: /submit?id=job_1709804965351&input_promoter_data=pBAD+0.0082+2.5+ACTTTTCATACTCCCGCCATTCAGAGAAGAAACCAATTGTCCATATTGCATCAGACATTGCCGTCACTGCGTCTTTTACTGGCTCTTCTCGCTAACCAAACCGGTAACCCCGCTTATTAAAAGCATTCTGTAACAAAGCGGGACCAAAGCCATGACAAAAACGCGTAACAAAAGTGTCTATAATCACGGCAGAAAAGTCCACATTGATTATTTGCACGGCGTCACACTTTGCTATGCCATAGCATTTTTATCCATAAGATTAGCGGATCCTACCTGACGCTTTTTATCGCAACTCTCTACTGTTTCTCCATACCCGTTTTTTTGGGCTAGC%0ApTac+0.0034+2.8+AACGATCGTTGGCTGTGTTGACAATTAATCATCGGCTCGTATAATGTGTGGAATTGTGAGCGCTCACAATT%0A&output_gene_data=RFP+CTGAAGTGGTCGTGATCTGAAACTCGATCACCTGATGAGCTCAAGGCAGAGCGAAACCACCTCTACAAATAATTTTGTTTAATACTAGAGTCACACAGGAAAGTACTAGATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAACAGATAAAAAAAATCCTTAGCTTTCGCTAAGGATGATTTCT%0A&verilog_text=module+A%28output+out1%2C++input+in1%2C+in2%29%3B%0A++always%40%28in1%2Cin2%29%0A++++begin%0A++++++case%28%7Bin1%2Cin2%7D%29%0A++++++++2%27b00%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b01%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b10%3A+%7Bout1%7D+%3D+1%27b0%3B%0A++++++++2%27b11%3A+%7Bout1%7D+%3D+1%27b1%3B%0A++++++endcase%0A++++end%0Aendmodule%0A (Caused by NewConnectionError('<urllib3.connection.HTTPConnection object at 0x0000029CEFCF7610>: Failed to establish a new connection: [WinError 10061] Aucune connexion n’a pu être établie car l’ordinateur cible l’a expressément refusée'))
        ```
        

- FINALLY WORKED (by executing the source code)!!!!! The problem was that the abcOutput.bench file was not created. Had to modify the following files (but also had to create another computer session with selena instead of Séléna because the special characters were a pain in the a**, maybe there is no need for the changes with this new username)
    - Line 129 ABCadaptator
        
        ```jsx
        commandBuilder = new StringBuilder("cd " + resourcesFilePath.substring(0, resourcesFilePath.length()-1) + " && " + resourcesFilePath + "abc.exe -c \"read " + resultsFilepath + filename + ".blif; strash;  rewrite; refactor; balance; write " + resultsFilepath + "abcOutput.bench; quit\" && " + "cd " + current_directory);
        ```
        
        instead of 
        
        ```jsx
        commandBuilder = new StringBuilder(resourcesFilePath + "abc.exe -c \"read " + resultsFilepath + filename + ".blif; strash;  rewrite; refactor; balance; write " + resultsFilepath + "abcOutput.bench; quit\"");
        ```
        
    
    - In NetSynth lines 1002 - 1004
        
        ```jsx
        String true_results_path = this.resultsPath.substring(0,17) + this.resultsPath.substring(18);
        String new_results_path = System.*getProperty*("user.dir")+ "\\" + true_results_path;
        abcoutput = ABCAdaptor.*runABC*("Blif_File_EsABC", this.resourcesPath, new_results_path, this.wirecount);
        ```
        
    
- Now everything works but don’t have the visual respresentations, get the error :
    
    ```jsx
    =========== SBOL for circuit plasmids ========
    org.sbolstandard.core2.SBOLValidationException: sbol-10522: The sequenceAnnotations property of a ComponentDefinition MUST NOT contain two or more SequenceAnnotation objects that refer to the same Component.
    Reference: SBOL Version 2.1.0 Section 7.7 on page 22
    : http://cellocad.org/job_1710246547216_A000_P000_circuit/sequence_annotation_backbone_component_23
    at org.sbolstandard.core2.SequenceAnnotation.setComponent(SequenceAnnotation.java:538)
    at org.cellocad.adaptors.sboladaptor.SBOLCircuitWriter.setAnnotations(SBOLCircuitWriter.java:115)
    at org.cellocad.adaptors.sboladaptor.SBOLCircuitWriter.writeSBOLCircuit(SBOLCircuitWriter.java:54)
    at org.cellocad.MIT.dnacompiler.DNACompiler.generatePlasmids(DNACompiler.java:1319)
    at org.cellocad.MIT.dnacompiler.DNACompiler.run(DNACompiler.java:1068)
    at org.cellocad.MIT.dnacompiler.CelloMain.main(CelloMain.java:15)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:566)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:293)
    at java.base/java.lang.Thread.run(Thread.java:834)
    org.sbolstandard.core2.SBOLValidationException: sbol-12003: The FunctionalComponent referenced by the participant property of a Participation MUST be contained by the ModuleDefinition that contains the Interaction which contains the Participation.
    Reference: SBOL Version 2.1.0 Section 7.9.4 on page 44
    : http://cellocad.org/job_1710246547216_A000_P000_module/pTac_produces_SrpR_protein/pTac
    at org.sbolstandard.core2.Interaction.addParticipation(Interaction.java:315)
    at org.sbolstandard.core2.Interaction.createParticipation(Interaction.java:170)
    at org.sbolstandard.core2.Interaction.createParticipation(Interaction.java:295)
    at org.sbolstandard.core2.Interaction.createParticipation(Interaction.java:249)
    at org.sbolstandard.core2.Interaction.createParticipation(Interaction.java:191)
    at org.cellocad.adaptors.sboladaptor.SBOLCircuitWriter.setProductionInteractions(SBOLCircuitWriter.java:307)
    at org.cellocad.adaptors.sboladaptor.SBOLCircuitWriter.writeSBOLCircuit(SBOLCircuitWriter.java:66)
    at org.cellocad.MIT.dnacompiler.DNACompiler.generatePlasmids(DNACompiler.java:1319)
    at org.cellocad.MIT.dnacompiler.DNACompiler.run(DNACompiler.java:1068)
    at org.cellocad.MIT.dnacompiler.CelloMain.main(CelloMain.java:15)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.base/java.lang.reflect.Method.invoke(Method.java:566)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:293)
    at java.base/java.lang.Thread.run(Thread.java:834)
    ```
    
    Lines 114 -> 128 in SBOLCircuitWriter 
    
    ```
         // Check if the component already has a SequenceAnnotation
            boolean hasAnnotation = false;
            for (SequenceAnnotation existingAnnotation : _circuit_component_definition.getSequenceAnnotations()) {
                if (existingAnnotation.getComponent().equals(c.getIdentity())) {
                    hasAnnotation = true;
                    break;
                }
            }
    
            if (!hasAnnotation) {
                SequenceAnnotation sequenceAnnotation = _circuit_component_definition.createSequenceAnnotation(annotationID, "locationID"+_annotation_index, current_bp, next_bp);
                sequenceAnnotation.setComponent(c.getIdentity());
    
                _annotation_index++;
            }
    
    ```
    
    instead of 
    
    ```jsx
    SequenceAnnotation sequenceAnnotation = _circuit_component_definition.createSequenceAnnotation(annotationID, "locationID"+_annotation_index, current_bp, next_bp);
                sequenceAnnotation.setComponent(c.getIdentity());
    ```
    
    Then for the error :
    
    ```jsx
    org.sbolstandard.core2.SBOLValidationException: sbol-12003: The FunctionalComponent referenced by the participant property of a Participation MUST be contained by the ModuleDefinition that contains the Interaction which contains the Participation.
    Reference: SBOL Version 2.1.0 Section 7.9.4 on page 44
    ```
    
    Need to change all the calls to the functions .createParticipation, the second argument needs to be .getIdentity(), for example protein_fc.getIdentity() instead of proteinParticipationID
    

—> Everything worked except for the images of the circuit 

- Problem with the function *writeCircuitsForDNAPlotLib* line 1330 in [DNACompiler.java](http://DNACompiler.java) —> it calls the python script /resources/scripts/plot_SBOL_designs.py incorrectly
    - Had to change the command to execute from
        
        ```jsx
        python_exe + " -W ignore " + options.get_home() + "/resources/scripts/plot_SBOL_designs.py";
        ```
        
        to 
        
        ```jsx
        python_exe + " -W ignore " + options.get_home().substring(1) + "/resources/scripts/plot_SBOL_designs.py";
        ```
        
    - Had to change the python script : instead of ‘rU’ option as argument in the open function I used ‘r’ (lines 47, 60, 83, 119)

WORKS PERFECTLY
