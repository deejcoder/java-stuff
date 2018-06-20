xquery version "3.0";

(:~
: User: Dylan Tonks (16058989)
: Date: 27/03/2018
: Time: 7:13 PM
: To change this template use File | Settings | File Templates.
:)

for $x in doc('xml/MemberData.xml')//committees/committee
let $majority := number($x/ratio/majority)
let $minority := number($x/ratio/minority)
where $majority eq $minority*2 and $majority ne 0 (:~ Note: question said "is" not "greater" else than eq, gt:)
return $x