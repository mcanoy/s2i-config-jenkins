import hudson.model.*
import jenkins.model.*
import hudson.slaves.*
import hudson.plugins.sshslaves.verifiers.*

def macOSXNodeURL = System.getenv('NODE_MAC_OSX_URL')

if(macOSXNodeURL != null) {
  println 'Create Mac OSX node'

  SshHostKeyVerificationStrategy hostKeyVerificationStrategy = new NonVerifyingKeyVerificationStrategy()

  ComputerLauncher launcher = new hudson.plugins.sshslaves.SSHLauncher(
        macOSXNodeURL, // Host
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
        "jenkins-slave-ios",
        "/Users/admin",
        launcher)
  agent.nodeDescription = "Mac OSX node"
  agent.numExecutors = 2
  agent.labelString = "jenkins-slave-ios"
  agent.mode = Node.Mode.EXCLUSIVE
  agent.retentionStrategy = new RetentionStrategy.Always()

  // Create a "Permanent Agent"
  Jenkins.instance.addNode(agent)

  println "Node has been created successfully."

}
