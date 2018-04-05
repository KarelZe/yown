# Tag last commit as 'latest'.

if [ "$TRAVIS_BRANCH" = "master" -a "$TRAVIS_PULL_REQUEST" = "false" ]; then
  git config --global user.email "hi@travis-ci.org"
  git config --global user.name "Sr. Travis"

  git remote add release "https://${GH_TOKEN}@github.com/KarelZe/yown.git"

  git push -d release latest
  git tag -d latest
  git tag -a "latest" -m "This is the latest version pushed to the master branch."
  git push release --tags
fi