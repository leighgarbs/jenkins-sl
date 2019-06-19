#!groovy

def call(args)
{
    // Clone the repository containing the code to test.  Not sure if this is
    // somehow better than just doing the clone in the shell.
    checkout changelog: true,
             poll: true,
             scm: [$class: 'GitSCM',
                   branches: [[name: env.BRANCH_NAME]],
                   browser: [$class: 'GitLab',
                              repoUrl: args[0],
                              version: '$GITLAB_VERSION'],
                   extensions: [[$class: 'SubmoduleOption',
                                 disableSubmodules: false,
                                 parentCredentials: false,
                                 recursiveSubmodules: true,
                                 reference: '',
                                 trackingSubmodules: false],
                                [$class: 'RelativeTargetDirectory',
                                 relativeTargetDir: 'workdir']],
                   submoduleCfg: [],
                   userRemoteConfigs: [[credentialsId: '', url: args[0]]]]

    // Check out any other repositories we need
    for (repoUrl in args[1])
    {
        sh returnStatus: true, script: 'git clone ' + repoUrl
    }
}
