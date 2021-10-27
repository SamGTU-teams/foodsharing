MODULES="analyzer"
MODULES="$MODULES parser"

GENERATED="generated"

for MODULE in ${MODULES}; do
    echo ${MODULE};
    touch "$MODULE/$GENERATED/__init__.py"
    mkdir -p "$MODULE/$GENERATED/platform"
    cp -rf "platform/python/." "$MODULE/$GENERATED/platform/"
done
