mkdir tmp
cd lab0
echo -e "\nAnswer 4.1:"
touch /tmp/466217
wc -c *6 */*6 2>/tmp/466217 | sort
echo -e "\nAnswer 4.2:"
ls -R bagon4 2>&1 | sort -k 9 | grep -v '^$'
echo -e "\nAnswer 4.3:"
grep -ir "e$" misdreavus2 2>/dev/null
echo -e "\nAnswer 4.4:"
ls -Rl 2>&1 | sort -k 9 | grep 'e$'
echo -e "\nAnswer 4.5:"
grep -vi "pe" cherrim6 2>&1
echo -e "\nAnswer 4.6:"
ls -lR *6 */*6 2>/dev/null | head -n 4 | sort -k 2
