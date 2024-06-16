Repo-URL:   https://github.com/sgra64/se1.bestellsystem.git
Repo-ssh:   git@github.com:sgra64/se1.bestellsystem.git

Create a new repository, add remote and push branch
  git remote add origin git@github.com:sgra64/se1.bestellsystem.git
  git push --set-upstream origin main

Check-out single branch:
  git clone -b libs --single-branch https://github.com/sgra64/se1.play.git libs
  git remote rename origin sgra64

Reset commits on remote branch
  git reset --hard <commit_id>
  git push --force
