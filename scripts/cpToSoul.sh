#!/bin/sh

pushd target >> /dev/null
scp original-percentage-apbt-1.0.jar percentage-apbt-1.0.jar soul:~/tibet/lib/
popd >> /dev/null
