import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import hudson.plugins.sshslaves.verifiers.*

// Format http://url.com@@node_name@@label, http://url2.com@@node_name2@@label

def macOSXNodeURL = System.getenv('NODE_MAC_OSX_URL')

if(macOSXNodeURL != null) {
  println 'Create Mac OSX nodes'

  String[] macNodes = macOSXNodeURL.split(",");

  for(int i=0; i < macNodes.length; i++) {

    String[] nodeInfo = macNodes[i].split("@@");

    SshHostKeyVerificationStrategy hostKeyVerificationStrategy = new NonVerifyingKeyVerificationStrategy()

    ComputerLauncher launcher = new hudson.plugins.sshslaves.SSHLauncher(
        nodeInfo[0].trim(), // Host
        22, // port
        System.getenv('OPENSHIFT_BUILD_NAMESPACE') + "-" + "mac-osx-key-id", // Credentials ID
        (String)null, // JVM Options
        (String)null, // JavaPath
        "source ~/.bash_profile && ", // Prefix Start Slave Command
        (String)null, // Suffix Start Slave Command
        (Integer)null,// Connection Timeout in Seconds
        (Integer)null,// Maximum Number of Retries
        (Integer)null,// The number of seconds to wait between retries
        hostKeyVerificationStrategy // Host Key Verification Strategy
    )

    // Define a "Permanent Agent"
    Slave agent = new DumbSlave(
          nodeInfo[1].trim(),
          "/Users/admin",
          launcher)
    agent.nodeDescription = "Mac OSX node"
    agent.numExecutors = 2
    agent.labelString = nodeInfo[2].trim();
    agent.mode = Node.Mode.EXCLUSIVE
    agent.retentionStrategy = new RetentionStrategy.Always()

    // Create a "Permanent Agent"
    Jenkins.instance.addNode(agent)

    println "Node has been created successfully."
 }
}
