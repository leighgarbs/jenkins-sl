#!groovy

package stage

class StageCheckout extends Stage
{
    String            repo_under_test
    ArrayList<String> other_repos

    // Constructor
    StageCheckout(def               wfc,
                  String            repo_under_test,
                  ArrayList<String> other_repos = [],
                  String            name = 'CHECKOUT')
    {
        // Satisfy the parent constructor
        super(wfc, name)

        this.repo_under_test = repo_under_test
        this.other_repos = other_repos
    }

    boolean body()
    {
        // This clone the git repository we'll be building and testing.  Not
        // sure if this is somehow better than just doing the clone in the
        // shell.
        wfc.checkout changelog: true,
                     poll: true,
                     scm: [$class: 'GitSCM',
                           branches: [[name: wfc.env.BRANCH_NAME]],
                           browser: [$class: 'GitLab',
                                     repoUrl: repo_under_test,
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
                           userRemoteConfigs: [[credentialsId: '',
                                                url: repo_under_test]]]

        // These repositories have things we need for other stages, but aren't
        // themselves under test
        for (repo in other_repos)
        {
            // Fail if check out does not work
            def returnCode = sh returnStatus: true, script: repo

            if (returnCode != 0)
            {
                return false
            }
        }

        return true
    }
}
