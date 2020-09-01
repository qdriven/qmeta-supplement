#!/usr/bin/env bash


cd docs_origin
mkdocs build
cd ../

rm -rf docs
mkdir docs
mv docs_origin/site/* docs

git add .
git commit -m "update docs"
git push