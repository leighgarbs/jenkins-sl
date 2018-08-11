#!groovy

def call()
{
  sh '''
    ARTIFACTS_DIR=artifacts
    ARTIFACTS_STAGE_DIR=$ARTIFACTS_DIR/$STAGE_NAME

    mkdir -p "$ARTIFACTS_DIR"
    rm -rf "$ARTIFACTS_STAGE_DIR"
    mkdir -p "$ARTIFACTS_STAGE_DIR"
    cd workdir
    git ls-files -o --directory | xargs -n 1 -I{} cp -a --parents {} \
      "../$ARTIFACTS_STAGE_DIR"
  '''
}
