#!groovy

package stage

class StageCheckout extends Stage
{
    String git_repo

    // Some of the stages use utilities out of this repository
    def binRepo = 'git clone http://gitlab.dmz/leighgarbs/bin.git'

    // Constructor
    StageCheckout(def wfc,
                  String git_repo,
                  boolean cleanWorkspace = false,
                  String name = 'CHECKOUT')
    {
        // Satisfy the parent constructor
        super(wfc, name, cleanWorkspace)

        this.git_repo = git_repo
    }

    boolean runLinux()
    {
        // This clones the git repository we'll be building and testing.  Not
        // sure if this is somehow better than just doing the clone in the
        // shell.
        wfc.checkout changelog: true,
        poll: true,
        scm: [$class: 'GitSCM',
              branches: [[name: wfc.env.BRANCH_NAME]],
              browser: [$class: 'GitLab',
                        repoUrl: git_repo,
                        version: '$GITLAB_VERSION'],
              extensions: [[$class: 'SubmoduleOption',
                            disableSubmodules: false,
                            parentCredentials: false,
                            recursiveSubmodules: true,
                            reference: '',
                            trackingSubmodules: false]],
              submoduleCfg: [],
              userRemoteConfigs: [[credentialsId: '', url: git_repo]]]

        wfc.sh returnStatus: true, script: binRepo

        return true
    }

    boolean runWindows()
    {
        // This clones the git repository we'll be building and testing.  Not
        // sure if this is somehow better than just doing the clone in the
        // shell.
        wfc.checkout changelog: true,
        poll: true,
        scm: [$class: 'GitSCM',
              branches: [[name: wfc.env.BRANCH_NAME]],
              browser: [$class: 'GitLab',
                        repoUrl: git_repo,
                        version: '$GITLAB_VERSION'],
              extensions: [[$class: 'SubmoduleOption',
                            disableSubmodules: false,
                            parentCredentials: false,
                            recursiveSubmodules: true,
                            reference: '',
                            trackingSubmodules: false]],
              submoduleCfg: [],
              userRemoteConfigs: [[credentialsId: '', url: git_repo]]]

        wfc.bat returnStatus: true, script: binRepo

        return true
    }
}
