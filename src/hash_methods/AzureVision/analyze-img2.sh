#!/bin/bash
key="$(cat "$(pwd)/src/hash_methods/AzureVision/resource_key.txt")"
endpoint="https://analyze-image-foxik.cognitiveservices.azure.com/"

# Image file path
image_path="/c/Users/foxjo/Pictures/absolg.png"

if [ $# -gt 0 ]; then
	image_path=$(echo $1 | sed "s/đ/ /g")
fi

# Request headers
headers=(
    "Content-Type: application/octet-stream"
    "Ocp-Apim-Subscription-Key: $key"
)

# Request parameters
params="visualFeatures=Description"

# Make the API call
result=$(curl -X POST -H "${headers[0]}" -H "${headers[1]}" "$endpoint/vision/v3.2/analyze?$params" --data-binary "@$image_path")

if [ -n "$result" ]; then
    analysis=$(echo "$result" | jq -r '. | @json')
else
    echo "API call did not return a valid result."
    exit 1
fi

string="
$(echo -e "\n[[Description]]{"
echo "$analysis" | jq -r '.description.captions[].text'
echo "}")
"

echo "$string" >&2
echo "$string" | md5sum | awk '{print $1}'
