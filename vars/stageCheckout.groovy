#!groovy

def call(args)
{
  // This clone the git repository we'll be building and testing.  Not sure if
  // this is somehow better than just doing the clone in the shell.
  checkout changelog: true, poll: true, scm: [$class: 'GitSCM',
    branches: [[name: env.BRANCH_NAME]],
    browser: [$class: 'GitLab',
             repoUrl: args[0],
             version: '$GITLAB_VERSION'],
    extensions: [[$class: 'SubmoduleOption',
                disableSubmodules:   false,
                parentCredentials:   false,
                recursiveSubmodules: true,
                reference:           '',
                trackingSubmodules:  false],
                [$class: 'RelativeTargetDirectory',
                relativeTargetDir: 'workdir']],
    submoduleCfg: [],
    userRemoteConfigs: [[credentialsId: '', url: args[0]]]]

  // This repository has some scripts we need for later stages
  sh 'git clone http://gitlab.dmz/leighgarbs/bin.git'
}
