#!/bin/bash

# Check if correct number of arguments are provided
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <directory> <repository>"
	echo $1 $2 all $3 $4
    exit 1
fi

directory=$(echo $1 | sed "s/đ/ /g")
echo arg1= $directory
destination="./data/imgs"
repository=$2
pwd
# Copy directory to destination
if [ ! -d "$destination/$directory" ]; then
	cp -r "$directory" "$destination" || { echo "Error: Failed to copy directory."; exit 1; }
fi
pwd
# Initialize git repository if not already initialized
if [ ! -d .git ]; then
	git init || { echo "Error: Failed to initialize git repository."; exit 1; }
fi

# Change directory to destination
cd "$destination" || { echo "Error: Failed to change directory."; exit 1; }
pwd
# Add files to git
git add . || { echo "Error: Failed to add files to git."; exit 1; }

# Commit changes
git commit -m "added imgs" || { echo "Error: Failed to commit changes."; exit 1; }

# Push to remote repository
git remote add origin "$repository" || { echo "Error: Failed to add remote repository."; }
git push -u origin master || { echo "Error: Failed to push to remote repository."; exit 1; }

echo "Directory copied, committed, and pushed successfully."