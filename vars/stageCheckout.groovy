#!groovy

def call(args)
{
  deleteDir()

  checkout changelog: true, poll: true, scm: [$class: 'GitSCM',
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
    userRemoteConfigs: [[credentialsId: '',
                       url: args[0]]]]

  sh """
    git clone http://gitlab.dmz/leighgarbs/bin.git
  """
}
